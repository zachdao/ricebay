import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import mockAxios from 'jest-mock-axios';
import { AccountEdit } from './AccountEdit';
import { MemoryRouter } from 'react-router-dom';
import { defaultTheme, Provider } from '@adobe/react-spectrum';

const renderComponent = async (account) => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <AccountEdit account={account} />
            </MemoryRouter>
        </Provider>,
    );
};
describe('AccountEdit', () => {
    afterEach(() => {
        mockAxios.reset();
    });
    const testAccount = {
        id: 'abc123',
        givenName: 'test first name',
        surname: 'test last name',
        alias: 'test alias',
        email: 'test@rice.edu',
        zelleId: 'zelleid1234',
    };
    it('should post with updated account', async () => {
        const user = userEvent.setup();
        await renderComponent(testAccount);
        await user.type(screen.getByLabelText('First Name'), ' updated');
        await user.click(screen.getByText('Save'));
        mockAxios.mockResponse({
            data: {
                success: true,
                data: null,
                msg: 'OK',
            },
            status: 200,
        });
        expect(mockAxios.post).toHaveBeenCalledWith(
            `/accounts/${testAccount.id}`,
            { ...testAccount, givenName: `${testAccount.givenName} updated` },
        );
    });
});
