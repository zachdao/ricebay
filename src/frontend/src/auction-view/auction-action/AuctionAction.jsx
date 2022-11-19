import React, { useContext } from 'react';
import { UserContext } from '../../user.context';
import {Button, Content, Dialog, Flex, Heading, TextField, DialogTrigger} from "@adobe/react-spectrum";

export const AuctionAction = ({ auction }) => {
    const user = useContext(UserContext);

    // auction is not owned by session user
    if (auction?.ownerID != user.ID) {
        // if bid is still published
        if (auction?.published) {
            // TODO Not sure how to find if user has leading bid with available controller endpoints
            // when user isn't currently leading bid, option to place bid

            return (
                <DialogTrigger type="popover">
                    <Button alignSelf="center" variant="cta">
                        Increase Bid (Max: ${auction.maxBid})
                    </Button>
                    <Dialog>
                        <Heading>Place your bid</Heading>
                        <Content>
                            <Flex
                                direction="rows"
                                justifyContent="space-evenly"
                                alignContent="center"
                                marginTop="size-100"
                                gap="size-100"
                            >
                                <TextField
                                    validationState={
                                        error ? 'invalid' : undefined
                                    }
                                    label="Your bid"
                                    value={yourBid}
                                    onChange={setBid}
                                    type="double"
                                    onBlur={() => setError('')}
                                />
                                <TextField
                                    validationState={
                                        error ? 'invalid' : undefined
                                    }
                                    label="Max bid"
                                    value={maxBid}
                                    onChange={setMaxBid}
                                    type="double"
                                    onBlur={() => setError('')}
                                />
                                <Button
                                    alignSelf="center"
                                    variant="cta"
                                    isDisabled={!isValid()}
                                    onPress={makeBid}
                                >
                                    Make bid
                                </Button>
                            </Flex>
                        </Content>
                    </Dialog>
            )

            // if user is leading bid disable option to bid
        }
        // when bid is over
        else {
            // if user is winner
            // TODO not sure how to find if user has won auction with available controller endpoints
                // if no rating exists, give option to rate

                // else, give option to change seller rating

            // else disable action and display that bid is over
        }
    }
};
