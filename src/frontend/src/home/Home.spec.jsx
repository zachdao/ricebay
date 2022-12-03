import { act, render, screen, waitFor } from '@testing-library/react';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import { MemoryRouter } from 'react-router-dom';
import { AuctionList } from '../auction-list/AuctionList';
import mockAxios from 'jest-mock-axios';
import React from 'react';
import { Home } from './Home';
import { SearchContext } from '../search.context';

const renderComponent = async (searchText = '') => {
    await render(
        <Provider theme={defaultTheme}>
            <SearchContext.Provider value={searchText}>
                <MemoryRouter>
                    <Home />
                </MemoryRouter>
            </SearchContext.Provider>
        </Provider>,
    );
};

describe('Home', () => {
    const testAuctions = [
        {
            id: 'abc123',
            title: 'foobar',
            currentBid: 2,
            description: 'some description',
        },
    ];

    afterEach(() => {
        mockAxios.reset();
    });

    it('should call search with no query when there is no search text', async () => {
        await act(async () => await renderComponent());

        await waitFor(async () => {
            expect(mockAxios.request).toHaveBeenCalledWith({
                url: '/auctions/search',
            });
        });
    });

    it('should call search with additional queries when search text exists', async () => {
        const searchText = 'foobar';
        await act(async () => await renderComponent(searchText));

        await waitFor(async () => {
            expect(mockAxios.request).toHaveBeenCalledWith({
                url: `/auctions/search?title:like=${searchText}&description:like=${searchText}`,
            });
        });
    });
});
