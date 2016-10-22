CREATE KEYSPACE v1x1 WITH REPLICATION = {
    'class' : 'SimpleStrategy',
    'replication_factor' : 1
};

USE v1x1;

CREATE TABLE discord_channel (
    scheduleId text,
    display_name text,
    tenant_id uuid,
    PRIMARY KEY (scheduleId)
);

CREATE TABLE twitch_channel (
    scheduleId text,
    display_name text,
    tenant_id uuid,
    PRIMARY KEY (scheduleId)
);

CREATE TYPE global_user_entry (
    platform int,
    display_name text,
    user_id text
);

CREATE TABLE global_user (
    scheduleId uuid,
    "entries" list<frozen<global_user_entry>>,
    PRIMARY KEY (scheduleId)
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
    scheduleId uuid,
    "entries" list<frozen<tenant_entry>>,
    PRIMARY KEY (scheduleId)
);

CREATE TYPE permission (
    node text
);

CREATE TABLE tenant_user_permissions (
    tenant_id uuid,
    user_id uuid,
    permissions list<frozen<permission>>,
    PRIMARY KEY (tenant_id, user_id)
);

CREATE TABLE key_value_entry (
    name text,
    key blob,
    value blob,
    PRIMARY KEY ((name, key))
);

CREATE TABLE global_configuration (
    module text,
    json text,
    PRIMARY KEY (module)
);

CREATE TABLE tenant_configuration (
    module text,
    tenant_id uuid,
    json text,
    PRIMARY KEY ((module, tenant_id))
);

CREATE TABLE persistent_language (
    scheduleId uuid,
    parent_id uuid,
    name text,
    strings map<text, text>,
    PRIMARY KEY (scheduleId)
);