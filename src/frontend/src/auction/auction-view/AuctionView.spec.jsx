import React from 'react';
import {render, screen } from '@testing-library/react';
import { AuctionView } from './AuctionView';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import mockAxios from 'jest-mock-axios';
import {DateTime} from "luxon";
import {MemoryRouter} from "react-router-dom";

const auction = {
    title: 'Obnoxiously Long Title Blah Blah Blah',
    minimumBid: 20.0,
    published: 'Published',
    categories: ['Furniture'],
    description:
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ' +
        'labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut ' +
        'aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore ' +
        'eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt ' +
        'mollit anim id est laborum.',
    seller: {
        id: 12345,
        alias: 'coolguy25',
        rating: 3,
    },
    images: [],
    bidIncrement: 2.0,
    startDate: DateTime.now().minus({days: 1}).toISODate(),
    endDate: DateTime.now().plus({days: 1}).toISODate(),
};

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <AuctionView auction={auction} />
            </MemoryRouter>
        </Provider>,
    );
};

describe('AuctionView', () => {
    afterEach(() => {
        mockAxios.reset();
    });

    it('should render the auction details', async () => {
            await renderComponent();
            expect(screen.getByText(auction.title)).toBeInTheDocument();
        },
    );

    //TODO Implement more tests
});
