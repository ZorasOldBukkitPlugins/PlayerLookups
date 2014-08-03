SELECT pl_names.name, pl_names.timestamp FROM pl_uuids
    LEFT JOIN pl_names ON pl_uuids.id = pl_names.player_id
    WHERE pl_uuids.uuid=?
    ORDER BY `timestamp` ASC;