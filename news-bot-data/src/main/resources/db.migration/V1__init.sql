CREATE TABLE IF NOT EXISTS subscription
(
    id   bigserial NOT NULL PRIMARY KEY,
    name text      NOT NULL,
    url  text      NOT NULL
);

CREATE TABLE IF NOT EXISTS page
(
    id              bigserial NOT NULL PRIMARY KEY,
    subscription_id bigint
        CONSTRAINT page_subscription_id
            REFERENCES subscription
            DEFERRABLE,
    name            text      NOT NULL,
    description     text,
    url             text      NOT NULL,
    published_date  timestamptz,
    author          text
);

INSERT into subscription(name, url)
VALUES ('java-dzone', 'https://feeds.dzone.com/java');