CREATE KEYSPACE v1x1 WITH REPLICATION = {
    'class' : 'SimpleStrategy',
    'replication_factor' : 1
};

USE v1x1;

CREATE TABLE discord_channel (
    id text,
    display_name text,
    tenant_id uuid,
    PRIMARY KEY (id)
);

CREATE TABLE twitch_channel (
    id text,
    display_name text,
    tenant_id uuid,
    PRIMARY KEY (id)
);

CREATE TYPE global_user_entry (
    platform int,
    display_name text,
    user_id text
);

CREATE TABLE global_user (
    id uuid,
    entries list<global_user_entry>,
    PRIMARY KEY (id)
);

CREATE TABLE inverse_global_user (
    platform int,
    user_id text,
    global_user_id uuid,
    PRIMARY KEY ((platform, user_id))
);

CREATE TYPE tenant_entry (
    platform int,
    display_name text,
    channel_id text
);

CREATE TABLE tenant (
    id uuid,
    entries list<tenant_entry>,
    PRIMARY KEY (id)
);

CREATE TYPE permission (
    node text
);

CREATE TABLE tenant_user_permissions (
    tenant_id uuid,
    user_id uuid,
    permissions list<permission>,
    PRIMARY KEY (tenant_id, user_id)
);

