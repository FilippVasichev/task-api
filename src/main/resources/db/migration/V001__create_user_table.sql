CREATE TABLE IF NOT EXISTS "user" (
    id          UUID DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
    email       TEXT NOT NULL,
    password    TEXT NOT NULL,
    name        TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS user_email_index ON "user"(email);