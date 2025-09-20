---Create Database---

  CREATE TABLE categories (
      category_id INTEGER PRIMARY KEY,
      category_name VARCHAR(100)
  );
  
  CREATE TABLE projects (
      project_id SERIAL PRIMARY KEY,
      project_name VARCHAR(255),
      funding_goal NUMERIC(15,2),
      deadline DATE,
      current_funding NUMERIC(15,2),
      category_id INTEGER,
      reject_count INTEGER,
      FOREIGN KEY (category_id) REFERENCES categories(category_id)
  );
  
  CREATE TABLE reward_tiers (
      tier_id INTEGER PRIMARY KEY,
      project_id INTEGER,
      tier_name VARCHAR(255),
      min_pledge_amount NUMERIC(15,2),
      quantity INTEGER,
      FOREIGN KEY (project_id) REFERENCES projects(project_id)
  );
  
  CREATE TABLE users (
      user_id INTEGER PRIMARY KEY,
      username VARCHAR(50),
      created_at TIMESTAMPTZ,
      password VARCHAR(255)
  );
  
  CREATE TABLE pledges (
      pledge_id SERIAL PRIMARY KEY,
      user_id INTEGER NOT NULL,
      project_id INTEGER NOT NULL,
      tier_id INTEGER,
      pledge_amount NUMERIC(15,2) NOT NULL,
      pledged_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
      
      FOREIGN KEY (user_id) REFERENCES users(user_id),
      FOREIGN KEY (project_id) REFERENCES projects(project_id),
      FOREIGN KEY (tier_id) REFERENCES reward_tiers(tier_id)
  );

---EXAMPLE DATA---
  INSERT INTO categories (category_id, category_name) VALUES
  (1, 'เทคโนโลยี'),
  (2, 'ศิลปะ'),
  (3, 'เกม');
  
  INSERT INTO projects (project_id, project_name, funding_goal, deadline, current_funding, category_id) VALUES
  ('10000001', 'Smart Home IoT', 50000, '2025-12-31', 12000, 1),
  ('10000002', 'AI Robot Helper', 80000, '2025-11-30', 80000, 1),
  ('10000003', 'Digital Art Gallery', 30000, '2025-10-15', 15000, 2),
  ('10000004', 'Street Art Festival', 40000, '2025-09-30', 40000, 2),
  ('10000005', 'Indie Game Jam', 25000, '2025-12-01', 5000, 3),
  ('10000006', 'Retro RPG Revival', 60000, '2025-11-15', 60000, 3),
  ('10000007', 'Wearable Health Tracker', 70000, '2025-12-20', 35000, 1),
  ('10000008', 'Virtual Reality Museum', 90000, '2025-12-25', 45000, 2);
  
  INSERT INTO reward_tiers (tier_id, project_id, tier_name, min_pledge_amount, quantity) VALUES
  (1, '10000001', 'Basic Supporter', 500, 100),
  (2, '10000001', 'Premium Kit', 2000, 50),
  (3, '10000001', 'VIP Sponsor', 5000, 10),
  (4, '10000002', 'Robot Sticker', 300, 200),
  (5, '10000002', 'Mini Robot', 1500, 80),
  (6, '10000002', 'Full Robot Set', 7000, 20),
  (7, '10000003', 'Digital Print', 200, 150),
  (8, '10000003', 'Art Book', 800, 60),
  (9, '10000003', 'VIP Pass', 2500, 10),
  (10, '10000004', 'Festival Ticket', 400, 300),
  (11, '10000004', 'Artist Meet', 1200, 40),
  (12, '10000004', 'Sponsor Banner', 5000, 5),
  (13, '10000005', 'Game Key', 150, 200),
  (14, '10000005', 'Beta Access', 600, 50),
  (15, '10000005', 'Producer Credit', 2000, 10),
  (16, '10000006', 'RPG Sticker', 250, 150),
  (17, '10000006', 'Collector Box', 1200, 40),
  (18, '10000006', 'VIP Edition', 4000, 8),
  (19, '10000007', 'Health Band', 700, 120),
  (20, '10000007', 'Smart Watch', 2500, 40),
  (21, '10000007', 'Lifetime Membership', 9000, 5),
  (22, '10000008', 'VR Pass', 500, 100),
  (23, '10000008', 'Museum T-shirt', 1200, 30),
  (24, '10000008', 'VIP VR Tour', 4000, 8);

-- ตรวจสอบผลลัพธ์
  SELECT 'Categories' as table_name, COUNT(*) as count FROM categories
  UNION ALL
  SELECT 'Users', COUNT(*) FROM users  
  UNION ALL
  SELECT 'Projects', COUNT(*) FROM projects
  UNION ALL  
  SELECT 'Reward Tiers', COUNT(*) FROM reward_tiers
  UNION ALL
  SELECT 'Pledges', COUNT(*) FROM pledges;
