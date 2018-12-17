# Stackoverflow archive imported as SQLite databases

StackOverflow publishes snapshot of its data periodically at archive.org [here](https://archive.org/download/stackexchange) and is [licensed under cc-by-sa 3.0](https://ia600107.us.archive.org/27/items/stackexchange/license.txt).

This repository hosts StackOverflow User data imported into a convenient SQLite database.  The date of snapshot is 3-Dec-2018.

# Usage

Download so_users.db (1.94GB) and it can be used with `sqlite3` command line tool or any other SQLite database management tool.  The schema is as follows:

```sql
CREATE TABLE so_users (Id integer primary key, DisplayName varchar, 
 Location varchar, Reputation integer, Views integer, 
 UpVotes integer, DownVotes integer, AccountId integer, 
 CreationDate datetime, LastAccessDate datetime, WebsiteUrl varchar, 
 AboutMe varchar) without rowid;
CREATE INDEX so_users_name on so_users (DisplayName);
CREATE INDEX so_users_loc on so_users (Location);
```

The records can be accessed using User Id, DisplayName or Location.

# Screenshots

![Screen shot 1](scrshot1.png?raw=true)

![Screen shot 1](scrshot2.png?raw=true)
