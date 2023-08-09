package com.cqrcb.dboptimize.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * name
     */
    private String name;

    /**
     * email
     */
    private String email;

    /**
     * phone
     */
    private String phone;

    /**
     * address
     */
    private String address;

    /**
     * created_at
     */
    private Date createdAt;

    /**
     * updated_at
     */
    private Date updatedAt;
}
