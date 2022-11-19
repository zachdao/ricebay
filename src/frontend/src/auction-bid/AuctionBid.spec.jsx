import React from "react";
import {render, screen} from "@testing-library/react";
import {AuctionBid} from "./AuctionBid";

describe('AuctionBid', () => {
    const testAuction = {title: "guitar",};


    it('should render an auction that can be bid on', async() => {
        await render(<AuctionBid />);
        expect(screen.queryByText(testAuction.title)).toBeInTheDocument();
    });
});