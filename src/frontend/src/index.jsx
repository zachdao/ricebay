import React from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { App } from './App';
import { NotFoundError } from './NotFoundError';
import { defaultTheme, Provider } from '@adobe/react-spectrum';

const router = createBrowserRouter([
    {
        path: '/',
        element: <App />,
        errorElement: <NotFoundError />,
    },
]);

const container = document.getElementById('root');
const root = createRoot(container);
root.render(
    <Provider theme={defaultTheme}>
        <RouterProvider router={router} />
    </Provider>,
);
