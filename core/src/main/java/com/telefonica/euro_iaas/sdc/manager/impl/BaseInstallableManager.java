/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.manager.impl;

import static com.telefonica.euro_iaas.sdc.util.Configuration.CHEF_DIRECTORY_COOKBOOK;
import static com.telefonica.euro_iaas.sdc.util.Configuration.DELETE_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.Configuration.UNTAR_COMMAND;
import static com.telefonica.euro_iaas.sdc.util.Configuration.UPLOAD_RECIPES_SCRIPT;

import static com.telefonica.euro_iaas.sdc.util.Configuration.WEBDAV_FILE_URL;

import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.googlecode.sardine.util.SardineException;
import com.telefonica.euro_iaas.sdc.dao.FileDao;

import com.telefonica.euro_iaas.sdc.exception.OpenStackException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.util.CommandExecutor;


public class BaseInstallableManager {

    private CommandExecutor commandExecutor;
   // protected SystemPropertiesProvider propertiesProvider;
    private FileDao fileDao;
    private OpenStackRegion openStackRegion;

    private String INSTALLABLE_NOT_FOUND = "404";
    private static Logger log = Logger.getLogger("BaseInstallableManager");

    // *************** METHODS RELATED TO CHEF SERVER ******************
    protected void uploadRecipe(File cookbook, String name) {
        String untarCommand = MessageFormat.format(UNTAR_COMMAND,
                cookbook.getAbsolutePath(), CHEF_DIRECTORY_COOKBOOK);
        try {
            log.log(Level.INFO, "untarCommand : " + untarCommand);
            commandExecutor.executeCommand(untarCommand);

            String uploadRecipeCommand = MessageFormat.format(UPLOAD_RECIPES_SCRIPT,
                    name);

            log.log(Level.INFO, "uploadRecipeCommand : " + uploadRecipeCommand);
            commandExecutor.executeCommand(uploadRecipeCommand);
            log.log(Level.INFO, "Recipe UPLOADED ");

        } catch (ShellCommandException e) {
            log.log(Level.SEVERE, e.getMessage());
            throw new SdcRuntimeException(e);
        }
        cookbook.deleteOnExit();

    }

    protected void deleteRecipe(String name, String version) {

        String deleteRecipeCommand = MessageFormat.format(DELETE_RECIPES_SCRIPT, name,
                version);

        try {
            log.log(Level.INFO, "deleteRecipeCommand : " + deleteRecipeCommand);
            commandExecutor.executeCommand(deleteRecipeCommand);
            log.log(Level.INFO, "Recipe DELETED ");
        } catch (ShellCommandException e) {
            log.log(Level.SEVERE, e.getMessage());
            throw new SdcRuntimeException(e);
        }
    }

    // **** FILE UPLOAD TO WEBDAV *****//

    protected void uploadInstallable(File installable, ReleaseDto releaseDto, String token) {
    	String webDavUrl = null;
		try {
			webDavUrl = openStackRegion.getWebdavPoint(token);
		} catch (OpenStackException e1) {
			log.log(Level.SEVERE, e1.getMessage());
            throw new SdcRuntimeException(e1);
		}
    	

        String webdavFileUrl = MessageFormat.format(WEBDAV_FILE_URL,
        		webDavUrl, releaseDto.getType(), releaseDto.getName(),
                releaseDto.getVersion());

        System.out.println(webdavFileUrl);

        createWebDavDirectoryStructure(releaseDto, token);
        try {
            fileDao.insertFile(webdavFileUrl, installable);
        } catch (SardineException e) {
            throw new SdcRuntimeException(e);
        } finally {
            installable.deleteOnExit();
        }
    }

    private void createWebDavDirectoryStructure(ReleaseDto releaseDto, String token) {
    	String webDavUrl = null;
		try {
			webDavUrl = openStackRegion.getWebdavPoint(token);
		} catch (OpenStackException e1) {
			log.log(Level.SEVERE, e1.getMessage());
            throw new SdcRuntimeException(e1);
		}

        log.log(Level.INFO, webDavUrl + "/" + releaseDto.getType() + "/"
                + releaseDto.getName() + "/");
        try {
            if (!fileDao.directoryExists(webDavUrl + "/" + releaseDto.getType()
                    + "/", webDavUrl + "/" + releaseDto.getType() + "/"
                    + releaseDto.getName() + "/"))

                fileDao.createDirectory(webDavUrl + "/" + releaseDto.getType()
                        + "/" + releaseDto.getName());
            else
                log.log(Level.INFO, "Directory " + webDavUrl + "/"
                        + releaseDto.getType() + "/" + releaseDto.getName() + "/" + " already CREATED");
        } catch (SardineException e) {
            throw new SdcRuntimeException(e);
        }

        try {
            if (!fileDao.directoryExists(webDavUrl + "/" + releaseDto.getType()
                    + "/" + releaseDto.getName() + "/", webDavUrl + "/"
                    + releaseDto.getType() + "/" + releaseDto.getName() + "/" + releaseDto.getVersion() + "/"))
                fileDao.createDirectory(webDavUrl + "/" + releaseDto.getType()
                        + "/" + releaseDto.getName() + "/" + releaseDto.getVersion());
            else
                log.log(Level.INFO, "Directory " + webDavUrl + "/"
                        + releaseDto.getType() + "/" + releaseDto.getName() + "/" + releaseDto.getVersion()
                        + " already CREATED");
        } catch (SardineException e) {
            throw new SdcRuntimeException(e);
        }
    }

    protected void deleteInstallable(ReleaseDto releaseDto, String token) {
    	String webDavUrl = null;
		try {
			webDavUrl = openStackRegion.getWebdavPoint(token);
		} catch (OpenStackException e1) {
			log.log(Level.SEVERE, e1.getMessage());
            throw new SdcRuntimeException(e1);
		}
        String webdavFileUrl = MessageFormat.format(WEBDAV_FILE_URL,
        		webDavUrl, releaseDto.getType(), releaseDto.getName(),
                releaseDto.getVersion());

        try {
            if (fileDao.directoryExists(webDavUrl + "/" + releaseDto.getType()
                    + "/" + releaseDto.getName() + "/" + releaseDto.getVersion() + "/", webdavFileUrl))

                fileDao.delete(webdavFileUrl);
            else
                log.log(Level.INFO,
                        "File " + webDavUrl + "/" + releaseDto.getType() + "/"
                                + releaseDto.getName() + "/" + " does NOT exist");
        } catch (SardineException e) {
            if (String.valueOf(e.getStatusCode()).equals(INSTALLABLE_NOT_FOUND)) {
                log.log(Level.INFO, webdavFileUrl + " does not EXIST");
            } else
                throw new SdcRuntimeException(e);
        }

        deleteWebDavDirectoryStructure(releaseDto, token);
    }

    private void deleteWebDavDirectoryStructure(ReleaseDto releaseDto, String token) {
    	String webDavUrl = null;
		try {
			webDavUrl = openStackRegion.getWebdavPoint(token);
		} catch (OpenStackException e1) {
			log.log(Level.SEVERE, e1.getMessage());
            throw new SdcRuntimeException(e1);
		}

        try {
            if (fileDao.directoryExists(webDavUrl + "/" + releaseDto.getType()
                    + "/" + releaseDto.getName() + "/", webDavUrl+ "/"
                    + releaseDto.getType() + "/" + releaseDto.getName() + "/" + releaseDto.getVersion() + "/"))

                fileDao.delete(webDavUrl + "/" + releaseDto.getType() + "/"
                        + releaseDto.getName() + "/" + releaseDto.getVersion() + "/");
            else
                log.log(Level.INFO, "Directory " + webDavUrl + "/"
                        + releaseDto.getType() + "/" + releaseDto.getName() + "/" + releaseDto.getVersion() + "/"
                        + " does NOT exist");
        } catch (SardineException e) {
            if (String.valueOf(e.getStatusCode()).equals(INSTALLABLE_NOT_FOUND)) {
                log.log(Level.INFO, webDavUrl + "/" + releaseDto.getType()
                        + "/" + releaseDto.getName() + "/" + " does not EXIST");
            } else
                throw new SdcRuntimeException(e);
        }

        try {
            if (fileDao.directoryExists(webDavUrl + "/" + releaseDto.getType()
                    + "/", webDavUrl + "/" + releaseDto.getType() + "/"
                    + releaseDto.getName() + "/"))

                fileDao.delete(webDavUrl + "/" + releaseDto.getType() + "/"
                        + releaseDto.getName() + "/");
            else
                log.log(Level.INFO, "Directory " + webDavUrl + "/"
                        + releaseDto.getType() + "/" + releaseDto.getName() + "/" + "  does NOT exist");
        } catch (SardineException e) {
            throw new SdcRuntimeException(e);
        }
    }


    /**
     * @param commandExecutor
     *            the commandExecutor to set
     */
    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    /**
     * @param FileDao
     *            the fileDao to set
     */
    public void setFileDao(FileDao fileDao) {
        this.fileDao = fileDao;
    }
    
    public void setOpenStackRegion (OpenStackRegion openStackRegion) {
    	this.openStackRegion = openStackRegion;
    }
}
