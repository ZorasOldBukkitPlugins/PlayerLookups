CREATE PROCEDURE pl_add_player (IN uuid VARCHAR(36), IN player_name VARCHAR(32), IN ip VARCHAR(16))
BEGIN	
    DECLARE most_recent_name VARCHAR(32);
    DECLARE most_recent_ip VARCHAR(32);
    DECLARE player_id INT UNSIGNED;
    DECLARE uuid_has_used_ip INT UNSIGNED;

    INSERT IGNORE INTO pl_uuids (`uuid`) VALUES (uuid);
    
    SELECT pl_uuids.id INTO player_id
        FROM pl_uuids
        WHERE pl_uuids.uuid=uuid
        LIMIT 1;

    SELECT pl_names.name INTO most_recent_name
        FROM pl_uuids
        LEFT JOIN pl_names ON pl_uuids.id = pl_names.player_id
        WHERE pl_uuids.uuid=uuid
        ORDER BY pl_names.`timestamp` DESC
        LIMIT 1;

    IF NOT (most_recent_name <=> player_name) THEN
        INSERT INTO pl_names (`player_id`, `name`) VALUES (player_id, player_name);
    ELSE
        UPDATE pl_names 
            SET pl_names.timestamp=now() 
            WHERE pl_names.name=player_name
            ORDER BY id DESC
            LIMIT 1;
    END IF;

    SELECT COUNT(*) INTO uuid_has_used_ip
        FROM pl_ips 
        WHERE pl_ips.player_id = player_id AND pl_ips.ip = ip;

    IF (uuid_has_used_ip = 0) THEN
        INSERT INTO pl_ips (`player_id`, `ip`) VALUES (player_id, ip);
    END IF;
END;