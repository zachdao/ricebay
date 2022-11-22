import {act, render, screen, waitFor} from "@testing-library/react";
import {defaultTheme, Provider} from "@adobe/react-spectrum";
import {MemoryRouter} from "react-router-dom";
import {Account} from "../account/Account";
import React from "react";
import {RecentlyViewed} from "./RecentlyViewed";
import mockAxios from "jest-mock-axios";
import {AuctionList} from "../auction-list/AuctionList";

const renderComponent = async () => {
    await render(
        <Provider theme={defaultTheme}>
            <MemoryRouter>
                <RecentlyViewed />
            </MemoryRouter>
        </Provider>,
    );
};

describe('RecentlyViewed', () => {
    afterEach(() => {
        mockAxios.reset();
    });

    it('should render list of recently viewed items', async () => {
        await renderComponent();

        expect(screen.queryByText('Recently Viewed')).toBeInTheDocument();
    });
});
