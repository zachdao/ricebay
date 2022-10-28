import React from 'react';
import { render, screen } from '@testing-library/react';
import { Login } from './Login';
import { MemoryRouter } from 'react-router-dom';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import userEvent from '@testing-library/user-event';
import mockAxios from 'jest-mock-axios';

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <Login />
            </MemoryRouter>
        </Provider>,
    );
};

describe('Login', () => {
    afterEach(() => {
        mockAxios.reset();
    });

    it('should render email and password text inputs and buttons for login and register', async () => {
        await renderComponent();

        expect(screen.queryByLabelText('Email')).toBeInTheDocument();
        expect(screen.queryByLabelText('Password')).toBeInTheDocument();
        expect(screen.queryByText('Login')).toBeInTheDocument();
        expect(screen.queryByText('Register')).toBeInTheDocument();
    });

    it('should call /accounts/login with field values when login is clicked', async () => {
        const user = userEvent.setup();

        await renderComponent();

        const email = 'test@rice.edu';
        const password = 'password';
        await user.type(screen.getByLabelText('Email'), email);
        await user.type(screen.getByLabelText('Password'), password);

        await user.click(screen.getByText('Login'));

        expect(mockAxios.post).toHaveBeenCalledWith('/accounts/login', {
            email,
            password,
        });
    });
});
