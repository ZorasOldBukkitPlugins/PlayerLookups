--Player Lookups--

This be a work in progress!

uuid, ip and name management system
- name lookups
- ip lookups
- uuid lookups

-- commands --
/lookup name [name] - looks up uuids and ips this name has gone under, along with the current uuid bound to this name (if any)
/lookup ip [ip] - looks up names and uuids this name has gone under
/lookup uuid [uuid] - looks up names and ips this uuid has gone under, along with the current name bound to this uuid

-- clipboard-like feature --
Players are able to 'copy' ips/uuids/names to their clipboard and insert them into any command. Each entry in a list of
data that the plugin returns is prefixed with a number, which the player will use to reference that data in their commands.
The data at each id will be referenced by typing 'uuid:#', 'name:#' or 'ip:#'.