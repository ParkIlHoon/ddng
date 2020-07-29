create table oauth_client_details
(
    client_id               varchar(256)    primary key,
    resource_ids            varchar(256),
    client_secret           varchar(256),
    scope                   varchar(256),
    authorized_grant_types  varchar(256),
    web_server_redirect_uri varchar(256),
    authorities             varchar(256),
    access_token_validity   integer,
    refresh_token_validity  integer,
    additional_information  varchar(4096),
    autoapprove             varchar(256)
);

insert into oauth_client_details
(
  client_id
, client_secret
, resource_ids
, scope
, authorized_grant_types
, web_server_redirect_uri
, authorities
, access_token_validity
, refresh_token_validity
, additional_information
, autoapprove
)
values
(
  'auth_id'
, '{noop}auth_secret'
, null
, 'read,write'
, 'authorization_code,password,client_credentials,implicit,refresh_token'
, null
, 'ROLE_MY_CLIENT'
, 36000
, 2592000
, null
, null
);