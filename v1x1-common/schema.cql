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
    "entries" list<frozen<global_user_entry>>,
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
    "entries" list<frozen<tenant_entry>>,
    PRIMARY KEY (id)
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
    id uuid,
    parent_id uuid,
    name text,
    strings map<text, text>,
    PRIMARY KEY (id)
);

CREATE TABLE interval_schedule (
    id uuid,
    module blob,
    payload blob,
    interval bigint,
    PRIMARY KEY (id)
);

CREATE TABLE cron_schedule (
    id uuid,
    module blob,
    payload blob,
    minute set<int>,
    hour set<int>,
    day_of_month set<int>,
    month set<int>,
    day_of_week set<int>,
    PRIMARY KEY (id)
);

CREATE TYPE configuration_definition_field (
    display_name text,
    description text,
    default_value text,
    config_type int,
    requires list<text>,
    tenant_permission int,
    json_field text
);

CREATE TABLE global_configuration_definition (
    name text,
    display_name text,
    description text,
    version int,
    tenant_permission int,
    fields list<frozen<configuration_definition_field>>,
    PRIMARY KEY (name)
);

CREATE TABLE tenant_configuration_definition (
    name text,
    display_name text,
    description text,
    version int,
    tenant_permission int,
    fields list<frozen<configuration_definition_field>>,
    PRIMARY KEY (name)
);