import React from 'react';
import { Header } from './Header';
import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

describe('Header', () => {
    it('should render with three grid areas', async () => {
        render(<Header />);
        expect(screen.queryByTestId('menu-area')).toBeInTheDocument();
        expect(screen.queryByTestId('search-area')).toBeInTheDocument();
        expect(screen.queryByTestId('profile-area')).toBeInTheDocument();
    });

    it('should call "menuClicked" when the menu-area is clicked', async () => {
        const user = userEvent.setup();
        const spy = jest.fn();
        render(<Header menuClicked={spy} />);
        const menu = screen.getByTestId('menu-area');

        await user.click(menu);

        expect(spy).toHaveBeenCalledTimes(1);
    });
});
