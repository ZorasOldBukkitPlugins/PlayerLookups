SELECT pl_uuids.uuid, pl_names.timestamp FROM pl_names
    LEFT JOIN pl_uuids ON pl_uuids.id = pl_names.player_id
    WHERE pl_names.name=?;