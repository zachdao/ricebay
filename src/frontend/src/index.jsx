import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { App } from './App';
import { NotFoundError } from './NotFoundError';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import { Login } from './login/Login';
import { Toaster } from 'react-hot-toast';
import { Register } from './register/Register';

const container = document.getElementById('root');
const root = createRoot(container);
root.render(
    <Provider theme={defaultTheme}>
        {/* set up routing based on URL path */}
        <BrowserRouter>
            {/* switch on path */}
            <Routes>
                {/* path = / */}
                <Route
                    index
                    element={<App />}
                    errorElement={<NotFoundError />}
                />
                {/* path = /login */}
                <Route
                    path="login"
                    element={<Login />}
                    errorElement={<NotFoundError />}
                />
                {/* path = /register */}
                <Route
                    path="register"
                    element={<Register />}
                    errorElement={<NotFoundError />}
                />
                {/* path = * */}
                <Route
                    path="*"
                    element={<App />}
                    errorElement={<NotFoundError />}
                />
            </Routes>
        </BrowserRouter>
        <Toaster position="bottom-center" />
    </Provider>,
);
