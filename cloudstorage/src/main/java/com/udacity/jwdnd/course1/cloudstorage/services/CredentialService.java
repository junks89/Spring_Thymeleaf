package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private final UserMapper userMapper;
    private final CredentialMapper credentialMapper;

    public CredentialService(UserMapper userMapper, CredentialMapper credentialMapper) {
        this.userMapper = userMapper;
        this.credentialMapper = credentialMapper;
    }

    public void addCredential(String url, String username, String key, String password, String authUsername) {
        Integer userId = userMapper.getUser(authUsername).getUserId();
        Credentials credential = new Credentials(0, url, username, key, password, userId);
        credentialMapper.insert(credential);
    }

    public Credentials[] getCredentialList(Integer userId) {
        return credentialMapper.getCredentialList(userId);
    }

    public Credentials getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }

    public void updateCredential(Integer credentialId, String username, String url, String key, String password) {
        credentialMapper.updateCredential(credentialId, username, url, key, password);
    }
}