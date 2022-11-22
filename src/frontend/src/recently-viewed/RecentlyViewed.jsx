import {Button, Flex, Heading, Text} from "@adobe/react-spectrum";
import React from "react";
import {AuctionList} from "../auction-list/AuctionList";

export const RecentlyViewed = () => {

    return(
        <Flex
            direction="column"
            alignItems="center"
            justifyContent="center"
        >
            <Heading level="1">
                Recently Viewed
            </Heading>
            {/*
            Using AuctionList as a placeholder
            Want to eventually return the auctions a user recently viewed
            */}
            <AuctionList/>
        </Flex>
    );
};