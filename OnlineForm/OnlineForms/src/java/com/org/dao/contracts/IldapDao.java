package com.org.dao.contracts;

import javax.naming.directory.DirContext;

public interface IldapDao extends IDao{
    public DirContext getContext(String userid, String password);
}
