--Player Lookups--

This be a work in progress!

uuid, ip and name management system
- name lookups
- ip lookups
- uuid lookups

-- commands --
/lookup ip(s) [uuid] - gets a list of ips a uuid has used
/lookup name(s) [uuid] - gets a list of names a uuid has used
/lookup uuid(s) [ip] - gets a list of uuids an ip has used
/lookup uuid(s) [name] - gets a list of uuids a name has used

-- clipboard-like feature --
Players are able to 'copy' ips/uuids/names to their clipboard and insert them into any command. Each entry in a list of
data that the plugin returns is prefixed with a number, which the player will use to reference that data in their commands.
The data at each id will be referenced by typing 'uuid:#', 'name:#' or 'ip:#'.