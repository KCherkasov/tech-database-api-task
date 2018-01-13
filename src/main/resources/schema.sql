-- noinspection SqlResolveForFile

SET SYNCHRONOUS_COMMIT = 'off';

CREATE EXTENSION IF NOT EXISTS CITEXT;

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE IF NOT EXISTS users (
  id SERIAL  PRIMARY KEY,
  about TEXT,
  email CITEXT UNIQUE NOT NULL,
  fullname TEXT NOT NULL,
  nickname CITEXT UNIQUE
);
DROP INDEX IF EXISTS nickname_lwr_idx;
DROP INDEX IF EXISTS nickname_idx;
CREATE INDEX nickname_lwr_idx ON users(LOWER(nickname));
CREATE INDEX nickname_idx ON users(nickname);

DROP TABLE IF EXISTS forums CASCADE;
CREATE TABLE IF NOT EXISTS forums (
  id SERIAL PRIMARY KEY,
  posts BIGINT DEFAULT 0,
  slug CITEXT UNIQUE NOT NULL,
  threads INTEGER DEFAULT 0,
  title TEXT NOT NULL,
  uid INTEGER REFERENCES users(id) ON DELETE CASCADE NOT NULL,
  uname CITEXT NOT NULL
);
DROP INDEX IF EXISTS lwr_fslug_idx;
CREATE INDEX lwr_fslug_idx ON forums(LOWER(slug));

DROP TABLE IF EXISTS forum_visitors CASCADE;
CREATE TABLE IF NOT EXISTS forum_visitors (
  uid INTEGER REFERENCES users(id) ON DELETE CASCADE NOT NULL,
  fid INTEGER REFERENCES forums(id) ON DELETE CASCADE NOT NULL,
  UNIQUE (uid, fid)
);
DROP INDEX IF EXISTS fid_visitors_idx;
CREATE INDEX fid_visitors_idx ON forum_visitors(fid);

DROP TABLE IF EXISTS threads CASCADE;
CREATE TABLE IF NOT EXISTS threads (
  uid INTEGER REFERENCES users(id) ON DELETE CASCADE NOT NULL,
  uname CITEXT NOT NULL,
  created TIMESTAMPTZ DEFAULT now(),
  fid INTEGER REFERENCES forums(id) ON DELETE CASCADE NOT NULL,
  fslug CITEXT NOT NULL,
  id SERIAL PRIMARY KEY,
  message TEXT NOT NULL,
  slug CITEXT UNIQUE DEFAULT NULL,
  title TEXT NOT NULL,
  votes INTEGER DEFAULT 0
);
DROP INDEX IF EXISTS threads_fslug_lwr_created_idx;
DROP INDEX IF EXISTS threads_fid_idx;
CREATE INDEX threads_fslug_lwr_created_idx ON threads(LOWER(fslug), created);
CREATE INDEX threads_fid_idx ON threads(fid);

DROP TABLE IF EXISTS posts CASCADE;
CREATE TABLE IF NOT EXISTS posts (
  id BIGSERIAL PRIMARY KEY,
  uid INTEGER REFERENCES users(id) ON DELETE CASCADE NOT NULL,
  uname CITEXT NOT NULL,
  fid INTEGER REFERENCES forums(id) ON DELETE CASCADE NOT NULL,
  fslug CITEXT NOT NULL,
  message TEXT NOT NULL,
  is_edited BOOLEAN NOT NULL DEFAULT FALSE,
  created TIMESTAMPTZ DEFAULT now() NOT NULL,
  pid BIGINT DEFAULT 0 NOT NULL,
  tid INTEGER REFERENCES threads(id) ON DELETE CASCADE,
  path BIGINT[] NOT NULL,
  rid BIGINT NOT NULL
);
DROP INDEX IF EXISTS posts_uid_idx;
DROP INDEX IF EXISTS posts_tid_id_idx;
DROP INDEX IF EXISTS posts_tid_id_idx;
DROP INDEX IF EXISTS posts_rid_path_idx;
DROP INDEX IF EXISTS posts_tid_pid_path_idx;
DROP INDEX IF EXISTS posts_path_rid_idx;
CREATE INDEX posts_uid_idx ON posts(uid);
CREATE INDEX posts_tid_id_idx ON posts(tid, id);
CREATE INDEX posts_tid_path_idx ON posts(tid, path);
CREATE INDEX posts_rid_path_idx ON posts(rid, path);
CREATE INDEX posts_tid_pid_path_idx ON posts(tid, pid, path);
CREATE INDEX posts_path_rid_idx ON POSTS(path, rid);

DROP TABLE IF EXISTS votes CASCADE;
CREATE TABLE IF NOT EXISTS votes (
  uid INTEGER REFERENCES users(id) ON DELETE CASCADE NOT NULL,
  tid INTEGER REFERENCES threads(id) ON DELETE CASCADE NOT NULL,
  voice SMALLINT DEFAULT 0,
  UNIQUE (uid, tid)
);
