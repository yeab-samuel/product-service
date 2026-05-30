ALTER TABLE products ADD COLUMN slug VARCHAR(255);

UPDATE products SET slug = lower(replace(name, ' ', '-'));

ALTER TABLE products ALTER COLUMN slug SET NOT NULL;
CREATE UNIQUE INDEX idx_products_slug ON products(slug);
