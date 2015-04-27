package com.blackducksoftware.qa.webdriver.twom.regression;

import java.util.Arrays;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.blackducksoftware.qa.webdriver.library.Verifier;
import com.blackducksoftware.qa.webdriver.library.abstraction.ButtonType;
import com.blackducksoftware.qa.webdriver.library.abstraction.StatusMessage;
import com.blackducksoftware.qa.webdriver.twom.TwoMBeanHelper;
import com.blackducksoftware.qa.webdriver.twom.TwoMConstants;
import com.blackducksoftware.qa.webdriver.twom.TwoMDriverTestBase;
import com.blackducksoftware.qa.webdriver.twom.TwomLdapMessages;
import com.blackducksoftware.qa.webdriver.twom.beans.administration.LdapBean;
import com.blackducksoftware.qa.webdriver.twom.consts.administration.LdapTestConnectionResult;
import com.blackducksoftware.qa.webdriver.twom.crud.administration.LdapPageActions;
import com.blackducksoftware.qa.webdriver.twom.crud.administration.TestConnectionPageActions;
import com.blackducksoftware.qa.webdriver.twom.forms.administration.AdministrationLdapIntegrationForm;
import com.blackducksoftware.qa.webdriver.twom.tab.TwoMHeader;

public class LdapTest extends TwoMDriverTestBase {

    private LdapPageActions ldapPageActions = new LdapPageActions();

    private TestConnectionPageActions testConnectionPageActions = new TestConnectionPageActions();

    private LdapBean originalLdap;

    @BeforeClass(alwaysRun = true)
    public void beforeLdapTestClass() {
        originalLdap = TwoMBeanHelper.initLdap();
        ldapPageActions.updateAndSave(originalLdap);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethodLdapTest() {
        logInAsAnotherUser(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        ldapPageActions.updateAndSave(originalLdap);
    }

    @Test(description = "Verify that enabling LDAP is functional 1.4.2")
    public void testEnablingLdapIsFunctional() {
        LdapBean ldap = TwoMBeanHelper.initLdapWithTestUser();
        ldapPageActions.updateAndSave(ldap);
        logInAsAnotherUser(ldap.getTestUsername(), ldap.getTestUserPassword());
        Verifier.get().verifyTrue(TwoMHeader.globalSearch.isVisible(),
                "1.4.2 FAILED.Login was unsuccessful");
    }

    @Test(description = "Verify that, when disabling LDAP, logging in with LDAP users is no longer accepted 1.4.3")
    public void testLoginWhenLdapDisabled() {
        LdapBean ldap = TwoMBeanHelper.initLdapWithTestUser();
        ldap.setEnableLdap(false);
        ldapPageActions.updateAndSave(ldap);
        logInAsAnotherUser(ldap.getTestUsername(), ldap.getTestUserPassword(),
                TwoMConstants.BAD_CREDENTIALS_MESSAGE);
        Verifier.get().verifyFalse(TwoMHeader.globalSearch.isVisible(),
                "1.4.3 FAILED .Login was successful");
    }

    @Test(description = "Verify that LDAP attribute mapping is functional 1.4.4")
    public void testLdapAttributeMapping() {
        LdapBean ldap = TwoMBeanHelper.initLdapWithTestUser();
        ldapPageActions.updateAndSave(ldap);
        testConnectionPageActions.clickTestConnection();
        LdapBean actualLdap = ldapPageActions.readDetails();
        Verifier.get().verifyEquals(actualLdap.getMappedFirstName(), ldap.getMappedFirstName(),
                "1.4.4 FAILED Expected mappedFirstName does not coincide with actual one");
        Verifier.get().verifyEquals(actualLdap.getMappedLastName(), ldap.getMappedLastName(),
                "1.4.4 FAILED Expected mappedLastName does not coincide with actual one");
        Verifier.get().verifyEquals(actualLdap.getMappedEmail(), ldap.getMappedEmail(),
                "1.4.4 FAILED Expected mappedEmail does not coincide with actual one");

    }

    @Test(description = "Verify that corresponding inline errors occur , if try to save when Group Synchronization is enabled but Group search base , Group filter , Group name attributes are missing .1.4.6")
    public void testInLineErrorsInLdapGroups() {
        final LdapBean initLdap = TwoMBeanHelper.initLdap();
        initLdap.setGroupFilter("");
        initLdap.setGroupNameAttr("");
        initLdap.setGroupSearchBase("");
        ldapPageActions.navigateToPage();
        new AdministrationLdapIntegrationForm() {

            @Override
            protected void execute() {
                setIgnoreSave();
                _synchronizeLdapGroups.check(initLdap.getSynchronizeLdapGroups());
                _groupFilter.type(initLdap.getGroupFilter());
                _groupNameAttribute.type(initLdap.getGroupNameAttr());
                _groupSearchBase.type(initLdap.getGroupSearchBase());
                click(ButtonType.SAVE);
                StatusMessage.readAndAssertContains(
                        Arrays.asList(
                                TwomLdapMessages.GROUP_FILTER_REQUIRED,
                                TwomLdapMessages.GROUP_NAME_ATTRIBUTE_REQUIRED,
                                TwomLdapMessages.GROUP_SEARCH_BASE_REQUIRED),
                        "Wrong Inline Errors when no Group search base , Group filter , Group name attributes are introduced ");

            }

            @Override
            protected void checkStatusMessage() {

            }
        };

    }

    @Test(description = "Verify that corresponding inline errors occur , if try to save when Server URL , User Search Base , User Search Filter are not introduced")
    public void testInlineErrorsWhenUrlSearchBaseAndFilterMissing() {
        final LdapBean initldap = TwoMBeanHelper.initLdap();
        initldap.setServerUrl("");
        initldap.setUserSearchBase("");
        initldap.setUserSearchFilter("");
        ldapPageActions.navigateToPage();
        new AdministrationLdapIntegrationForm() {

            @Override
            protected void execute() {
                setIgnoreSave();
                _synchronizeLdapGroups.check(false);
                _serverUrl.type(initldap.getServerUrl());
                _userSearchFilter.type(initldap.getUserSearchFilter());
                _userSearchBase.type(initldap.getUserSearchBase());
                click(ButtonType.SAVE);
                StatusMessage.readAndVerifyContains(Arrays.asList(TwomLdapMessages.SERVER_URL_REQUIRED, TwomLdapMessages.USER_SEARCH_BASE_REQUIRED,
                        TwomLdapMessages.USER_SEARCH_FILTER_REQUIRED),
                        "Wrong Inline errors when try to save without ServerURL , UserSearchBase and UserSearchFilter");

            }

            @Override
            protected void checkStatusMessage() {

            }
        };
    }

    @Test(description = "Verify Error Message  when wrong LDAP mapping attributes introduced ")
    public void testErrorMessageWithWrongLdapMappingAttributes() {
        LdapBean initLdap = TwoMBeanHelper.initLdapWithTestUser();
        LdapBean actualLdap = new LdapBean();
        initLdap.setFirstName("xxx");
        initLdap.setLastName("xxx");
        initLdap.setEmail("xxx");
        initLdap.setMappedEmail(TwomLdapMessages.FIELD_DOES_NOT_EXIST);
        initLdap.setMappedFirstName(TwomLdapMessages.FIELD_DOES_NOT_EXIST);
        initLdap.setMappedLastName(TwomLdapMessages.FIELD_DOES_NOT_EXIST);
        ldapPageActions.updateAndSave(initLdap);
        TestConnectionPageActions testconnection = new TestConnectionPageActions();
        testconnection.clickTestConnection();
        actualLdap = ldapPageActions.readDetails();
        Verifier.get().verifyEquals(actualLdap.getMappedFirstName(), initLdap.getMappedFirstName(),
                "FAILED Expected mappedFirstName does not coincide with actual one");
        Verifier.get().verifyEquals(actualLdap.getMappedLastName(), initLdap.getMappedLastName(),
                "FAILED Expected mappedLastName does not coincide with actual one");
        Verifier.get().verifyEquals(actualLdap.getMappedEmail(), initLdap.getMappedEmail(),
                "FAILED Expected mappedEmail does not coincide with actual one");

    }

    @Test(description = "Verify error message when try to test Ldap Connection with wrong server URL details")
    public void testLdapTestConnectionWithInvalidServerURL() {
        final LdapBean initldap = TwoMBeanHelper.initLdapWithTestUser();
        initldap.setServerUrl("mamba-vm.blackducksoftware.com:389");
        ldapPageActions.navigateToPage();
        new AdministrationLdapIntegrationForm() {

            @Override
            protected void execute() {
                setIgnoreSave();
                _serverUrl.type(initldap.getServerUrl());
                _authenticationType.selectOption(initldap.getAuthenticationType().getLabel());
                _userSearchBase.type(initldap.getUserSearchBase());
                _userSearchFilter.type(initldap.getUserSearchFilter());
                _synchronizeLdapGroups.check(false);
                _testUsername.type(initldap.getTestUsername());
                _testPassword.type(initldap.getTestUserPassword());
                click(ButtonType.SAVE);
                StatusMessage.dismiss();

            }
        };
        testConnectionPageActions.setExpectedStatusMessageOnNextClick(TwomLdapMessages.SERVER_URI_INVALID);
        LdapTestConnectionResult result = testConnectionPageActions.clickTestConnection();
        Verifier.get()
                .verifyEquals(result, LdapTestConnectionResult.FAIL, "FAILED:Test Connection results do not coincide , when invalid Rever Url introduced");
    }

    @Test(description = "Verify error message when try to test Ldap Connection with wrong user credentials ")
    public void testLdapConnectionWithInvalidUserCredentials() {
        final LdapBean initLdap = TwoMBeanHelper.initLdapWithTestUser();
        initLdap.setTestUsername("test");
        ldapPageActions.navigateToPage();
        new AdministrationLdapIntegrationForm() {

            @Override
            protected void execute() {
                setIgnoreSave();
                _serverUrl.type(initLdap.getServerUrl());
                _authenticationType.selectOption(initLdap.getAuthenticationType().getLabel());
                _userSearchBase.type(initLdap.getUserSearchBase());
                _userSearchFilter.type(initLdap.getUserSearchFilter());
                _synchronizeLdapGroups.check(false);
                _testUsername.type(initLdap.getTestUsername());
                _testPassword.type(initLdap.getTestUserPassword());
                click(ButtonType.SAVE);
                StatusMessage.dismiss();
            }
        };
        testConnectionPageActions.setExpectedStatusMessageOnNextClick(TwomLdapMessages.INVALID_TEST_USER_CREDENTIALS);
        LdapTestConnectionResult result = testConnectionPageActions.clickTestConnection();
        Verifier.get().verifyEquals(result, LdapTestConnectionResult.FAIL,
                "FAILED:Test Connection results do not coincide , when invalid user credentials introduced");
    }
    // @Test(description="Verified that groups synchronization is not performed when enable checkbox is not selected")
    // public void testGroupSynchronizationWhenItIsDisabled(){
    // LdapBean initLdap = TwoMBeanHelper.initLdap();
    // initLdap.setSynchronizeLdapGroups(false);
    // ldapPageActions.navigateToPage();
    // }

}
