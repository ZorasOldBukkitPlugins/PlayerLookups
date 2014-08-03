SELECT pl_uuids.uuid FROM pl_ips
    LEFT JOIN pl_uuids ON pl_uuids.id = pl_ips.player_id
    WHERE pl_ips.ip=?;