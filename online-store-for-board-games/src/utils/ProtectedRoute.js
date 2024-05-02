// ProtectedRoute.js
import React from 'react';
import { Route, Navigate } from 'react-router-dom';

const ProtectedRoute = ({ element: Element, isAuthenticated, ...rest }) => (
  <Route
    {...rest}
    element={isAuthenticated ? <Element /> : <Navigate to="/login" replace />}
  />
);

export default ProtectedRoute;