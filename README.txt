ELECTRONIC STORE MANAGEMENT SYSTEM

1. LOGIN CREDENTIALS:
   Administrator: username=admin, password=admin123
   [Add any test Manager/Cashier accounts created]

2. FEATURES:
   - Multi-level user system (Admin/Manager/Cashier)
   - Inventory management with categories and suppliers
   - Real-time stock alerts
   - Bill generation with automatic stock updates
   - Performance statistics and financial reporting
   - Sector-based organization

3. USAGE INSTRUCTIONS:
   - Run Main.java
   - Login with credentials above
   - Administrator can create Managers and Cashiers
   - Manager can add items, suppliers, view stats
   - Cashier can create bills for customers

4. DESIGN DECISIONS:
   - MVC architecture for separation of concerns
   - Binary files for system data, text files for bills
   - Stock alert threshold set to 3 items by default
   - Bill filename format: BILL-[number]_[date].txt

5. NON-SYLLABUS FEATURES:
   [List any extra features like advanced UI animations, additional reports, etc.]