CREATE TABLE todo_item (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    done BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_todo_item_title_not_blank
      CHECK (btrim(title) <> ''),
    CONSTRAINT ck_todo_item_priority
      CHECK (priority IN ('High', 'Medium', 'Low'))
);


INSERT INTO todo_item (title, description, priority, done, created_at, updated_at)
VALUES
    ('Review starter layout', 'Align shared layout blocks and page shells.', 'High', FALSE,
        CURRENT_TIMESTAMP - INTERVAL '3 hour', CURRENT_TIMESTAMP - INTERVAL '3 hour'),
    ('Prepare demo content', 'Add realistic sample cards for the dashboard.', 'Medium', FALSE,
        CURRENT_TIMESTAMP - INTERVAL '2 hour', CURRENT_TIMESTAMP - INTERVAL '2 hour'),
    ('Check build pipeline', 'Confirm Maven and Vite builds stay green.', 'Low', TRUE,
        CURRENT_TIMESTAMP - INTERVAL '1 hour', CURRENT_TIMESTAMP - INTERVAL '1 hour');
