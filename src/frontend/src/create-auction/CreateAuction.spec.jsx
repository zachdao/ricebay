import React from 'react';
import { render, screen } from '@testing-library/react';
import { CreateAuction } from './CreateAuction';
import { MemoryRouter } from 'react-router-dom';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import userEvent from '@testing-library/user-event';
import mockAxios from 'jest-mock-axios';

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <CreateAuction />
            </MemoryRouter>
        </Provider>,
    );
};

describe('Create Auction', () => {
    afterEach(() => {
        mockAxios.reset();
    });

    it(
        'should render text inputs for title, starting bid, bid increment, and description.' +
            'should render buttons for publishing, cancelling, saving, and uploading image. ' +
            'should render fields for adding categories, categories, start and end dates',
        async () => {
            await renderComponent();

            expect(
                screen.queryByLabelText('Auction Title'),
            ).toBeInTheDocument();
            expect(screen.queryByLabelText('Starting Bid')).toBeInTheDocument();
            expect(
                screen.queryByLabelText('Minimum Bid Increment'),
            ).toBeInTheDocument();
            expect(
                screen.queryByLabelText('Auction Description'),
            ).toBeInTheDocument();

            expect(screen.queryByText('Publish')).toBeInTheDocument();
            expect(screen.queryByText('Cancel')).toBeInTheDocument();
            expect(screen.queryByText('Save')).toBeInTheDocument();
            expect(screen.queryByText('Upload Image')).toBeInTheDocument();

            expect(screen.queryByText('Add Category')).toBeInTheDocument();
            expect(screen.queryByText('Categories')).toBeInTheDocument();
            expect(screen.queryByText('Start Date')).toBeInTheDocument();
            expect(screen.queryByText('End Date')).toBeInTheDocument();
        },
    );

    it('should call /auctions with field values when publish is clicked', async () => {
        const auction = userEvent.setup();

        await renderComponent();

        const title = 'title';
        const category = 'category';
        const startDate = '1/1/2023';
        const endDate = '1/2/2023';
        const startingBid = '20.0';
        const bidIncrement = '2.0';
        const description = 'description';
        const image = '';

        await auction.type(screen.getByLabelText('Auction Title'), title);
        await auction.type(screen.getByLabelText('Starting Bid'), startingBid);
        await auction.type(
            screen.getByLabelText('Minimum Bid Increment'),
            bidIncrement,
        );
        await auction.type(
            screen.getByLabelText('Auction Description'),
            description,
        );
        await auction.type(screen.getByText('Upload Image'), image);
        await auction.type(screen.getByText('Categories'), category);
        await auction.type(screen.getByText('Start Date'), startDate);
        await auction.type(screen.getByText('End Date'), endDate);

        await auction.click(screen.getByText('Publish'));

        expect(mockAxios.post).toHaveBeenCalledWith('/auctions', {
            title,
            image,
            startingBid,
            bidIncrement,
            startDate,
            endDate,
            category,
            description,
        });
    });
});
