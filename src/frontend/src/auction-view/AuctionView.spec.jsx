import React from 'react';
import { act, render, screen, waitFor } from '@testing-library/react';
import { AuctionView } from './AuctionView';
import { MemoryRouter } from 'react-router-dom';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import userEvent from '@testing-library/user-event';
import mockAxios from 'jest-mock-axios';

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <AuctionView />
        </Provider>,
    );
};

describe('AuctionBid', () => {
    afterEach(() => {
        mockAxios.reset();
    });

    it(
        'should render text fields for title, current bid, minimum bid increment, status, seller, seller ' +
            'rating, categories, and description. should render image. should render button to increase bid and ' +
            'popover to place bid',
        async () => {
            const auction = {
                title: 'Obnoxiously Long Title Blah Blah Blah',
                minimumBid: 20.0,
                published: 'Published',
                ownerId: 12345,
                categoryIds: [1, 2, 3],
                description:
                    'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ' +
                    'labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut ' +
                    'aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore ' +
                    'eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt ' +
                    'mollit anim id est laborum.',
                seller: 'coolguy25',
                images: [],
                maxBid: 500.0,
                bidIncrement: 2.0,
            };

            await renderComponent();

            expect(
                screen.queryByDisplayValue(auction.title).toBeInTheDocument(),
            );
        },
    );

    //TODO Implement more tests
});
