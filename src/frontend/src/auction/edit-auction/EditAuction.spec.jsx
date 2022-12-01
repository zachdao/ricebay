import React from 'react';
import { render, screen } from '@testing-library/react';
import { EditAuction } from './EditAuction';
import { MemoryRouter } from 'react-router-dom';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import userEvent from '@testing-library/user-event';
import mockAxios from 'jest-mock-axios';
import { DateTime } from 'luxon';

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
    startDate: DateTime.now().minus({ days: 1 }).toISODate(),
    endDate: DateTime.now().plus({ days: 1 }).toISODate(),
};

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <EditAuction auction={auction} />
            </MemoryRouter>
        </Provider>,
    );
};

describe('Edit Auction', () => {
    afterEach(() => {
        mockAxios.reset();
    });

    it('should render a form to update auctions', async () => {
        await renderComponent();

        expect(screen.getByLabelText('Auction Title')).toBeInTheDocument();
        expect(screen.getByLabelText(/Starting Bid.*/)).toBeInTheDocument();
        expect(screen.getByLabelText('Bid Increment')).toBeInTheDocument();
        expect(
            screen.getByLabelText('Auction Description'),
        ).toBeInTheDocument();

        expect(screen.getByText('Publish')).toBeInTheDocument();
        expect(screen.getByText('Cancel')).toBeInTheDocument();
        expect(screen.getByText('Save')).toBeInTheDocument();
        expect(screen.getByText('Upload Image')).toBeInTheDocument();

        expect(screen.getByText('Categories')).toBeInTheDocument();
        expect(screen.getByText(/Auction Dates.*/)).toBeInTheDocument();
    });

    it('should save the auction with field values when saved is clicked', async () => {
        const auction = userEvent.setup();

        await renderComponent();

        const title = 'title';
        const startingBid = '20.0';
        const bidIncrement = '2.0';
        const description = 'description';

        await auction.type(screen.getByLabelText('Auction Title'), title);
        await auction.type(
            screen.getByLabelText(/Starting Bid.*/),
            startingBid,
        );
        await auction.type(
            screen.getByLabelText('Bid Increment'),
            bidIncrement,
        );
        await auction.type(
            screen.getByLabelText('Auction Description'),
            description,
        );

        await auction.click(screen.getByText('Save'));

        expect(mockAxios.post).toHaveBeenCalledWith(
            `/auctions/${auction.id}`,
            expect.anything(),
        );
    });
});
