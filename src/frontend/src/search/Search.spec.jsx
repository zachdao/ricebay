import { act, render, screen, waitFor } from '@testing-library/react';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import { MemoryRouter } from 'react-router-dom';
import { Account } from '../account/Account';
import React from 'react';
import { Search } from './Search';
import mockAxios from 'jest-mock-axios';
import { AuctionList } from '../auction-list/AuctionList';

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <Search />
            </MemoryRouter>
        </Provider>,
    );
};

describe('Search', () => {
    afterEach(() => {
        mockAxios.reset();
    });

    it('should render list of searched items', async () => {
        await renderComponent();

        expect(screen.queryByText('Categories')).toBeInTheDocument();
        expect(screen.queryByText('Sort')).toBeInTheDocument();
    });
});
