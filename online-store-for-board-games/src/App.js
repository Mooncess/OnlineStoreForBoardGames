import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';
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
import CreateArticlePage from './pages/CreateArticlePage';
import CatalogPage from './pages/CatalogPage';
import ArticlePage from './pages/ArtilcePage';
import WishListPage from './pages/WishListPage';
import BasketPage from './pages/BasketPage';
import CheckoutPage from './pages/CheckoutPage';

function AdminRoute({ element }) {
  const [isAdmin, setIsAdmin] = useState(null);
  
  useEffect(() => {
    const checkAdminRole = async () => {
      try {
        const response = await axios.get('http://localhost:8099/api/auth/is-admin', { withCredentials: true });
        if (response.status === 200) {
          setIsAdmin(true);
        } else {
          setIsAdmin(false);
        }
      } catch (error) {
        console.error('Error checking admin role:', error);
        setIsAdmin(false)
      }
    };
    
    checkAdminRole();
  }, []);

  if (isAdmin === null) {
    return <div>Loading...</div>;
  } else if (isAdmin) {
    console.log(isAdmin);
    return element;
  } else {
    console.log(isAdmin);
    return <Navigate to="/" replace />;
  }
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/test" element={<TestPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/registration" element={<RegistrationPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/admin/admin-panel" element={<AdminRoute element={<AdminPanelPage />} />} />
        <Route path="/admin/article-management" element={<AdminRoute element={<ArtilceManagementPage />} />} />
        <Route path="/admin/category-management" element={<AdminRoute element={<CategoryManagementPage />} />} />
        <Route path="/admin/order-management" element={<AdminRoute element={<OrderManagementPage />} />} />
        <Route path="/admin/user-management" element={<AdminRoute element={<UserManagementPage />} />} />
        <Route path="/admin/create-article" element={<AdminRoute element={<CreateArticlePage />} />} />
        <Route path="/" element={<CatalogPage />} />
        <Route path="/article/:id" element={<ArticlePage />} />
        <Route path="/wish-list" element={<WishListPage />} />
        <Route path="/basket" element={<BasketPage />} />
        <Route path="/checkout" element={<CheckoutPage />} />
      </Routes>
    </Router>
  );
}

export default App;