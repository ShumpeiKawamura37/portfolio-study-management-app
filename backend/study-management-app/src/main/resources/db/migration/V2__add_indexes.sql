--　データを整列(11/2)
-- ON テーブル名（列, 順番）

CREATE INDEX idx_study_logs_user_id_start_time ON study_logs (user_id, start_time);

CREATE INDEX idx_study_logs_category_id ON study_logs (category_id);

CREATE INDEX idx_categories_user_id_created_at ON categories (user_id, created_at);

CREATE INDEX idx_categories_parent_id ON categories (parent_id);
