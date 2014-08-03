SELECT pl_ips.ip FROM pl_uuids
    LEFT JOIN pl_ips ON pl_uuids.id = pl_ips.player_id
    WHERE pl_uuids.uuid=?;