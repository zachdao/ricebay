import React from 'react';
import { act, render, screen, waitFor } from '@testing-library/react';
import { Account } from './Account';
import mockAxios from 'jest-mock-axios';
import { MemoryRouter } from 'react-router-dom';
import { defaultTheme, Provider } from '@adobe/react-spectrum';

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <Account />
            </MemoryRouter>
        </Provider>,
    );
};
describe('Account', () => {
    const testAccount = {
        givenName: 'test first name',
        surname: 'test last name',
        alias: 'test alias',
        email: 'test@rice.edu',
        zelleId: 'zelleid1234',
    };
    afterEach(() => {
        mockAxios.reset();
    });

    it('should display the detail by default', async () => {
        await act(async () => await renderComponent());

        await waitFor(async () => {
            expect(mockAxios.request).toHaveBeenCalledWith({
                url: '/accounts/me',
            });
        });

        await act(async () => {
            mockAxios.mockResponse({
                data: {
                    success: true,
                    data: testAccount,
                    msg: 'OK',
                },
                status: 200,
            });
        });

        expect(screen.queryByText(testAccount.email)).toBeInTheDocument();
    });
});
