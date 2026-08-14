-- Create table tfile
CREATE TABLE tfile (
    id UUID PRIMARY KEY,
    module_code VARCHAR(255),
    entity_name VARCHAR(255),
    entity_id UUID,
    file_name VARCHAR(255),
    original_name VARCHAR(255),
    content_type VARCHAR(255),
    file_size BIGINT,
    bucket_name VARCHAR(255),
    object_key VARCHAR(255),
    version INTEGER,
    security_level_code VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),
    status VARCHAR(50)
);

-- Create table tfile_version
CREATE TABLE tfile_version (
    id UUID PRIMARY KEY,
    file_id UUID NOT NULL REFERENCES tfile(id),
    version INTEGER,
    object_key VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),
    status VARCHAR(50)
);

-- Create table tfile_tag
CREATE TABLE tfile_tag (
    id UUID PRIMARY KEY,
    file_id UUID NOT NULL REFERENCES tfile(id),
    tag_code VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),
    status VARCHAR(50)
);
