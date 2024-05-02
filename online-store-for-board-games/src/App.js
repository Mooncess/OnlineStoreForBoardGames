import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import TestPage from './pages/TestPage';
import LoginPage from './pages/LoginPage';
import RegistrationPage from './pages/RegistrationPage';
import ProfilePage from './pages/ProfilePage';
import AdminPanelPage from './pages/AdminPanelPage';
import ArtilceManagementPage from './pages/ArticleManagementPage';
import CategoryManagementPage from './pages/CategoryManagementPage';
import OrderManagementPage from './pages/OrderManagementPage';
import UserManagementPage from './pages/UserManagementPage';

function App() {
  return (
    <Router>
        <Routes>
          <Route path="/test" element={<TestPage/>} />
          <Route path="/login" element={<LoginPage/>} />
          <Route path="/register" element={<RegistrationPage/>} />
          <Route path="/profile" element={<ProfilePage/>} />
          <Route path="/admin/admin-panel" element={<AdminPanelPage/>} />
          <Route path="/admin/artilce-management" element={<ArtilceManagementPage/>} />
          <Route path="/admin/category-management" element={<CategoryManagementPage/>} />
          <Route path="/admin/order-management" element={<OrderManagementPage/>} />
          <Route path="/admin/user-management" element={<UserManagementPage/>} />
        </Routes>
    </Router>
  );
}

export default App;
