import React from 'react';
import { act, render, screen, waitFor } from '@testing-library/react';
import { AuctionList } from './AuctionList';
import mockAxios from 'jest-mock-axios';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import { MemoryRouter } from 'react-router-dom';

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <AuctionList />
            </MemoryRouter>
        </Provider>,
    );
};

describe('AuctionList', () => {
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

    it('should render a grid of auctions', async () => {
        await act(async () => await renderComponent(<AuctionList />));

        await waitFor(async () => {
            expect(mockAxios.request).toHaveBeenCalledWith({
                url: '/auctions/search',
            });
        });

        await act(async () => {
            mockAxios.mockResponse({
                data: {
                    success: true,
                    data: testAuctions,
                    msg: 'OK',
                },
                status: 200,
            });
        });

        expect(
            screen.queryByText(new RegExp(`.*${testAuctions[0].title}.*`)),
        ).toBeInTheDocument();
    });
});
