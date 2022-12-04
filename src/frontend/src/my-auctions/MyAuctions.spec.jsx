import React from 'react';
import { MyAuctions } from './MyAuctions';
import { screen, render } from '@testing-library/react';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import { MemoryRouter } from 'react-router-dom';
import { UserContext } from '../user.context';

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <UserContext.Provider
                    value={{ user: { alias: 'test', email: 'test@rice.edu' } }}
                >
                    <MyAuctions />
                </UserContext.Provider>
            </MemoryRouter>
        </Provider>,
    );
};

describe('MyAuctions', () => {
    it('should render with one flex area', async () => {
        await renderComponent();
        expect(screen.queryByTestId('auctions-area')).toBeInTheDocument();
    });
});
