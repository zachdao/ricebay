import React from 'react';
import { Header } from './Header';
import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import { MemoryRouter } from 'react-router-dom';
import mockAxios from 'jest-mock-axios';
import { UserContext } from '../user.context';

const renderComponent = async (menuClicked) => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <UserContext.Provider
                    value={{ user: { alias: 'test', email: 'test@rice.edu' } }}
                >
                    <Header menuClicked={menuClicked} />
                </UserContext.Provider>
            </MemoryRouter>
        </Provider>,
    );
};

describe('Header', () => {
    it('should render with four grid areas', async () => {
        await renderComponent();
        expect(screen.queryByTestId('menu-area')).toBeInTheDocument();
        expect(screen.queryByTestId('search-area')).toBeInTheDocument();
        expect(screen.queryByTestId('profile-area')).toBeInTheDocument();
        expect(screen.queryByTestId('create-area')).toBeInTheDocument();
    });

    it('should call "menuClicked" when the menu-area is clicked', async () => {
        const user = userEvent.setup();
        const spy = jest.fn();
        await renderComponent(spy);
        const menu = screen.getByTestId('menu-area');

        await user.click(menu);

        expect(spy).toHaveBeenCalledTimes(1);
    });

    it('should call "logout" when the profile-area and then logout is clicked', async () => {
        const user = userEvent.setup();
        await renderComponent();
        const profile = screen.getByTestId('profile-area');

        await user.click(profile);
        await user.click(screen.getByText('Sign Out'));

        expect(mockAxios.post).toHaveBeenCalledWith('/accounts/logout');
    });

    afterEach(() => {
        mockAxios.reset();
    });
});
