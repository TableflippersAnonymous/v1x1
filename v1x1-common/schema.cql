/* Baseline */
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

/* Version 2 */

DROP TABLE tenant_user_permissions;

CREATE TABLE channel_configuration (
    module text,
    tenant_id uuid,
    platform int,
    channel_id text,
    json text,
    PRIMARY KEY ((module, tenant_id), platform, channel_id)
);

CREATE TABLE channel_platform_mapping (
    platform int,
    channel_id text,
    platform_group text,
    group_id uuid,
    PRIMARY KEY ((platform, channel_id), platform_group)
);

CREATE TABLE tenant_group (
    tenant_id uuid,
    group_id uuid,
    name text,
    permissions list<frozen<permission>>,
    PRIMARY KEY (tenant_id, group_id)
);

CREATE TABLE tenant_groups_by_user (
    user_id uuid,
    tenant_id uuid,
    groups list<uuid>,
    PRIMARY KEY (user_id, tenant_id)
);

CREATE TABLE tenant_group_membership (
    tenant_id uuid,
    group_id uuid,
    user_id uuid,
    PRIMARY KEY ((tenant_id, group_id), user_id)
);

/* Version 3 */

DROP TABLE tenant_configuration_definition;
DROP TABLE global_configuration_definition;
DROP TYPE configuration_definition_field;

CREATE TYPE configuration_definition_field (
    display_name text,
    description text,
    default_value text,
    config_type int,
    requires list<text>,
    tenant_permission int,
    json_field text,
    complex_type text
);

CREATE TABLE global_configuration_definition (
    name text,
    display_name text,
    description text,
    version int,
    tenant_permission int,
    fields list<frozen<configuration_definition_field>>,
    complex_fields map<text, frozen<list<configuration_definition_field>>>,
    PRIMARY KEY (name)
);

CREATE TABLE tenant_configuration_definition (
    name text,
    display_name text,
    description text,
    version int,
    tenant_permission int,
    fields list<frozen<configuration_definition_field>>,
    complex_fields map<text, frozen<list<configuration_definition_field>>>,
    PRIMARY KEY (name)
);

CREATE TABLE channel_configuration_definition (
    name text,
    display_name text,
    description text,
    version int,
    tenant_permission int,
    fields list<frozen<configuration_definition_field>>,
    complex_fields map<text, frozen<list<configuration_definition_field>>>,
    PRIMARY KEY (name)
);

/* Version 4 */

CREATE TABLE third_party_credential (
    name text,
    credential blob,
    PRIMARY KEY (name)
);

CREATE TABLE twitch_oauth_token (
    global_user_id uuid,
    user_id text,
    oauth_token text,
    scopes list<text>,
    PRIMARY KEY (global_user_id, user_id)
);

CREATE TABLE twitch_user_information (
    name text,
    id bigint,
    staff boolean,
    display_name text,
    email text,
    partnered boolean,
    PRIMARY KEY (name)
);

/* Version 5 */

CREATE TABLE joined_twitch_channel (
    channel text,
    PRIMARY KEY (channel)
);

/* Version 6 */

CREATE TABLE naive_bayes_classifier (
    name text,
    stats_id uuid,
    min_words int,
    min_training int,
    PRIMARY KEY (name)
);

CREATE TABLE naive_bayes_classifier_stats (
    id uuid,
    trained counter,
    classified counter,
    classified_0 counter,
    classified_5 counter,
    classified_10 counter,
    classified_15 counter,
    classified_20 counter,
    classified_25 counter,
    classified_30 counter,
    classified_35 counter,
    classified_40 counter,
    classified_45 counter,
    classified_50 counter,
    classified_55 counter,
    classified_60 counter,
    classified_65 counter,
    classified_70 counter,
    classified_75 counter,
    classified_80 counter,
    classified_85 counter,
    classified_90 counter,
    classified_95 counter,
    classified_100 counter,
    unclassifiable counter,
    PRIMARY KEY (id)
);

CREATE TABLE naive_bayes_word_stats (
    id uuid,
    word text,
    good counter,
    bad counter,
    seen counter,
    PRIMARY KEY (id, word)
);

CREATE TABLE artificial_neural_network (
    name text,
    id uuid,
    training_ttl int,
    layout list<int>,
    PRIMARY KEY (name)
);

CREATE TABLE artificial_neural_network_training_entry (
    id uuid,
    training_id timeuuid,
    inputs list<float>,
    outputs list<float>,
    PRIMARY KEY (id, training_id)
);

CREATE TABLE artificial_neural_network_rdb (
    id uuid,
    data blob,
    PRIMARY KEY (id)
);

/* Version 7 */

CREATE TABLE quote (
    id int,
    tenant_id uuid,
    quote text,
    game text,
    date timestamp,
    deleted boolean,
    PRIMARY KEY (tenant_id, id)
);