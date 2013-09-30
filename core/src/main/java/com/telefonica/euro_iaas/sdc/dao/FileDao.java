package com.telefonica.euro_iaas.sdc.dao;

import java.io.File;

import com.googlecode.sardine.util.SardineException;

/**
 * Provides the methods to work with WebDav.
 * 
 * @author Jesus M. Movilla
 */
public interface FileDao {

    /**
     * Create the webdavDirectory directory into the webdav
     * 
     * @param the
     *            webdavDirectoryUrl to be created
     * @return void
     */
    void createDirectory(String webdavDirectoryUrl) throws SardineException;

    /**
     * Insert the File into the specified WebDavDirectory.
     * 
     * @param webdavDirectory
     * @param the
     *            file to be uploaded to the webdav
     * @return the file deleted
     */
    String insertFile(String webdavDirectory, File installable) throws SardineException;

    /**
     * Delete the Resource specified vi webdavUrl.
     * 
     * @param the
     *            URL of the resource to be deleted in webdav (it could be a file or a directory)
     * @return void.
     */
    void delete(String webdavUrl) throws SardineException;

    /**
     * Check if the directory specified by webdavUrle exists
     * 
     * @param the
     *            DirectoryBase to check if a directory exists
     * @param the
     *            directory to be searched
     * @return boolean.
     */
    boolean directoryExists(String directoryBase, String webdavUrl) throws SardineException;
}
