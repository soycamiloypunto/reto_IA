CREATE TABLE IF NOT EXISTS credit_requests (
    id UUID PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    requested_amount DECIMAL(19,4) NOT NULL,
    term_months INT NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    channel VARCHAR(100) NOT NULL,
    operation_number VARCHAR(100) NOT NULL,
    idempotency_key VARCHAR(200) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    rejection_reason VARCHAR(500),
    applicant_id VARCHAR(50),
    applicant_name VARCHAR(100),
    applicant_email VARCHAR(100),
    applicant_phone VARCHAR(50)
);

-- Added for O1: Idempotency uniqueness
CREATE UNIQUE INDEX IF NOT EXISTS idx_credit_req_idempotency ON credit_requests (idempotency_key);

CREATE TABLE IF NOT EXISTS outbox_events (
    id UUID PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(100) NOT NULL,
    type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_outbox_status ON outbox_events(status);
CREATE INDEX IF NOT EXISTS idx_credit_req_status ON credit_requests(status);
