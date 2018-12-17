# Stackoverflow archive imported as SQLite databases

StackOverflow publishes snapshot of its data periodically at archive.org [here](https://archive.org/download/stackexchange) and is [licensed under cc-by-sa 3.0](https://ia600107.us.archive.org/27/items/stackexchange/license.txt).

This repository hosts StackOverflow User data imported into a convenient SQLite database.  The date of snapshot is 3-Dec-2018.

# Usage

Download `so_users.7z.*` [7zip](https://www.7-zip.org/) archive split files from this repository into a folder and extract `so_users.db` from them using the following command:

```
7za.exe e so_users.7z.001
```

`so_users.db` (1.94GB) can then be used with `sqlite3` command line tool or any other SQLite database management tool.  The schema is as follows:

```sql
CREATE TABLE so_users (Id integer primary key, DisplayName varchar, 
 Location varchar, Reputation integer, Views integer, 
 UpVotes integer, DownVotes integer, AccountId integer, 
 CreationDate datetime, LastAccessDate datetime, WebsiteUrl varchar, 
 AboutMe varchar) without rowid;
CREATE INDEX so_users_name on so_users (DisplayName);
CREATE INDEX so_users_loc on so_users (Location);
```

The records can be quickly accessed using User Id, DisplayName or Location through the existing indices.

# Scripts

The scripts used for importing the snapshots into database have also been provided here (ParseSOData.java and ins_xml_data_into_db.sh).

# Screenshots

![Screen shot 1](scrshot1.png?raw=true)

![Screen shot 1](scrshot2.png?raw=true)
