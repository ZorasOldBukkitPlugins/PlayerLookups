--Player Lookups--

uuid, ip and name management system
- juggles all three
- name lookups
- ip lookups
- uuid lookups

-- commands --
/lookup name [name] - looks up uuids and ips this name has gone under, along with the current uuid bound to this name (if any)
/lookup ip [ip] - looks up names and uuids this name has gone under
/lookup uuid [uuid] - looks up names and ips this uuid has gone under, along with the current name bound to this uuid

-- clipboard-like feature --
when viewing uuids or names or ips, they will be added to a player's 'clipboard', and can be 'pasted' into commands.

For instance, the /ban command requires a uuid. The player would first to
'/lookup name MrZoraman'
and would get an output like this:
[0] 132453532-3567-3435-3535-35353535
[1] 535367864-5643-4567-4321-23456778 //(these are the uuids that the server has seen using that username)

Current uuid:
[2] 535367864-5643-4567-4321-23456778 //(this is the uuid currently using the name)

all of the listed uuids are 'copied' to the clipboard, so the player can either do
/ban 132453532-3567-3435-3535-35353535
or they could do
/ban uuid:0

The replaced constants would be:
uuid:#
name:#
ip:#


