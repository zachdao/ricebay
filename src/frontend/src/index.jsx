import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Route, Routes, useNavigate } from 'react-router-dom';
import { App } from './App';
import { NotFoundError } from './NotFoundError';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import { Login } from './login/Login';
import { Toaster } from 'react-hot-toast';
import { Register } from './register/Register';
import axios from 'axios';

const container = document.getElementById('root');
const root = createRoot(container);
const Root = () => {
    const navigate = useNavigate();
    axios.interceptors.response.use(
        (response) => response,
        (error) => {
            // https://axios-http.com/docs/interceptors
            // Any status codes that falls outside the range of 2xx cause this function to trigger
            // Do something with response error
            if (
                error.request.status === 401 &&
                !error.request.responseURL.endsWith('login')
            ) {
                navigate('/login');
            } else {
                return Promise.reject(error);
            }
        },
    );
    {
        /* switch on path */
    }
    return (
        <Routes>
            {/* path = / */}
            <Route index element={<App />} errorElement={<NotFoundError />} />
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
    );
};
root.render(
    <Provider theme={defaultTheme}>
        {/* set up routing based on URL path */}
        <BrowserRouter>
            <Root />
        </BrowserRouter>
        <Toaster position="bottom-center" />
    </Provider>,
);
