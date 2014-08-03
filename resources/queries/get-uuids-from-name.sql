SELECT pl_uuids.uuid FROM pl_names
    LEFT JOIN pl_uuids ON pl_uuids.id = pl_names.player_id
    WHERE pl_names.name=?;