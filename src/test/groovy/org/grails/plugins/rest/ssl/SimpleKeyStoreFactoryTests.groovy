/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.grails.plugins.rest.ssl

import spock.lang.Specification

/**
 * @author berngp
 */
class SimpleKeyStoreFactoryTests extends Specification {
    Map KEYSTORE_CONFIG = [:], TRUSTSTORE_CONFIG = [:]

    def setup() {
        File keyStoreFile = new File('grails-app/conf/resources/certs/keystore.jks'),
             trustStoreFile = new File('grails-app/conf/resources/certs/truststore.jks')

        KEYSTORE_CONFIG = [https: [keystore: [path: keyStoreFile.path, pass: 'test1234']]]
        TRUSTSTORE_CONFIG = [https: [truststore: [path: trustStoreFile.path, pass: 'test1234']]]

    }

    void testGetKeyStoreFromConf() {
        given:
        ConfigObject configObject = new ConfigObject()
        configObject.putAll(KEYSTORE_CONFIG)

        when:
        SimpleKeyStoreFactory sksf= new SimpleKeyStoreFactory()

        def keyStoreModel = sksf.getKeyStoreModel(configObject)

        then:
        assert keyStoreModel.keystore, "KeyStore expected"
    }

    void testGetKeyStoreFromDefault() {
        given:
        ConfigObject configObject = new ConfigObject()

        SimpleKeyStoreFactory.metaClass.getDefaultKeyStoreHome = {
            'grails-app/conf/resources/certs'
        }

        when:
        SimpleKeyStoreFactory sksf= new SimpleKeyStoreFactory()
        def keyStoreModel = sksf.getKeyStoreModel(configObject)

        then:
        assert keyStoreModel.keystore, "KeyStore expected "
        assert keyStoreModel.path == "grails-app/conf/resources/certs/.keystore"
    }

    void testGetTrustStoreFromConf() {
        given:
        ConfigObject configObject = new ConfigObject()
        configObject.putAll(TRUSTSTORE_CONFIG)

        when:
        SimpleKeyStoreFactory sksf= new SimpleKeyStoreFactory()
        def trustStoreModel = sksf.getTrustStoreModel(configObject)

        then:
        assert trustStoreModel.keystore, "KeyStore expected"
    }

    void testGetTrustStoreFromDefault() {
        given:
        ConfigObject configObject = new ConfigObject()

        SimpleKeyStoreFactory.metaClass.getDefaultTrustStoreHome = {
            'grails-app/conf/resources'
        }
        when:
        SimpleKeyStoreFactory sksf= new SimpleKeyStoreFactory()
        def trustStoreModel = sksf.getTrustStoreModel(configObject)

        then:
        assert trustStoreModel.keystore, "KeyStore expected"
        assert trustStoreModel.path == 'grails-app/conf/resources/truststore.jks'
    }
}
