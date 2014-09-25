__author__ = 'jfernandez'

"""
Imports all steps already defined and implemented in 'uninstall_product' feature
"""
from component.uninstall_product.features.uninstall_product import *


from lettuce import step, world
from commons.utils import wait_for_software_installed
from commons.constants import *
from nose.tools import assert_true


@step(u'a successful installed product with name "([^"]*)" and release "([^"]*)"')
def a_successful_installed_product_with_name_group1_and_release_group2(step, product_name, product_version):
    a_created_product_with_name_group1(step, product_name, product_version)
    provisioning_steps.i_install_the_product_in_the_vm(step)
    task_is_created(step)
    the_task_has_finished_with_status_group1(step, TASK_STATUS_VALUE_SUCCESS)


@step(u'the task is performed')
def the_task_is_performed(step):
    the_task_has_finished_with_status_group1(step, TASK_STATUS_VALUE_SUCCESS)


@step(u'the product installation status is "(ERROR|INSTALLED|UNINSTALLED)"')
def the_product_installation_status_is(step, status):
    provisioning_steps.the_product_installation_status_is(step, status)


@step(u'the product is uninstalled')
def the_product_is_uninstalled(step):
    file_name = PRODUCT_FILE_NAME_FORMAT.format(product_name=world.product_name,
                                                product_version=world.product_version,
                                                installator=world.cm_tool)

    assert_true(wait_for_software_installed(status_to_be_finished=False, file_name=file_name),
                "ERROR: SOFTWARE IS NOT UNINSTALLED")


@step(u'the product "([^"]*)" and release "([^"]*)" is uninstalled')
def then_the_product_group1_and_release_group2_is_uninstalled(step, product_name, product_release):
    file_name = PRODUCT_FILE_NAME_FORMAT.format(product_name=product_name,
                                                product_version=product_release,
                                                installator=world.cm_tool)
    assert_true(wait_for_software_installed(status_to_be_finished=False, file_name=file_name),
                "ERROR: SOFTWARE IS NOT UNINSTALLED")


@step(u'the product "([^"]*)" and release "([^"]*)" remains installed')
def then_the_product_group1_and_release_group2_remains_installed(step, product_name, product_version):
    file_name = PRODUCT_FILE_NAME_FORMAT.format(product_name=product_name,
                                                product_version=product_version,
                                                installator=world.cm_tool)

    assert_true(wait_for_software_installed(status_to_be_finished=True, file_name=file_name),
                "ERROR: SOFTWARE IS NOT INSTALLED")


@step(u'a uninstalled product with name "([^"]*)" and release "([^"]*)"')
def a_uninstalled_product_with_name_group1_and_release_group2(step, product_name, product_version):
    a_successful_installed_product_with_name_group1_and_release_group2(step, product_name, product_version)
    i_uninstall_the_installed_product_group1_and_release_group2(step, world.product_name, world.product_version)
    task_is_created(step)
    the_task_has_finished_with_status_group1(step, TASK_STATUS_VALUE_SUCCESS)


