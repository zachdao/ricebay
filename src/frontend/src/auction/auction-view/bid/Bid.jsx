import React, { useCallback, useContext, useState } from 'react';
import { UserContext } from '../../../user.context';
import {
    Button,
    Content,
    Dialog,
    Flex,
    Heading,
    DialogTrigger,
    NumberField,
} from '@adobe/react-spectrum';

export const Bid = ({ auction, makeBid, ...otherProps }) => {
    const [bid, setBid] = useState(auction?.userBid?.bid || 0);
    const [maxBid, setMaxBid] = useState(auction?.userBid?.maxBid || undefined);
    const { user } = useContext(UserContext);

    const ownsBid = user?.id && user.id === auction?.seller?.id;

    const isValid = useCallback(() => {
        return makeBid > 0 ? bid <= maxBid : true;
    }, [bid, maxBid]);

    /* TODO: This component needs extended to display the correct button and perform the correct on click action
             based on the user + auction state (i.e. user hasn't placed a bid and isn't the owner, show the bid button)
    // auction is not owned by session user
    if (auction?.ownerID != user.ID) {
    // if bid is still published
    if (auction?.published) {
        // TODO Not sure how to find if user has leading bid with available controller endpoints
        // when user isn't currently leading bid, option to place bid


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
    }*/

    // TODO Disable button if user currently has highest bid
    // TODO re-enable button and give notification if they have been outbid

    return (
        <DialogTrigger type="popover">
            <Button
                alignSelf="center"
                variant="cta"
                isDisabled={ownsBid}
                {...otherProps}
            >
                Bid
            </Button>
            <Dialog size="M">
                <Heading>Place your bid</Heading>
                <Content>
                    <Flex
                        direction="rows"
                        justifyContent="space-between"
                        alignItems="end"
                        marginTop="size-100"
                        gap="size-50"
                    >
                        <NumberField
                            validationState={!isValid() ? 'invalid' : undefined}
                            label="Your bid"
                            isRequired
                            value={bid}
                            onChange={setBid}
                            minValue={
                                (auction.currentBid || auction.minimumBid) +
                                (auction.bidIncrement || 0)
                            }
                            step={auction.bidIncrement || 0.5}
                            hideStepper
                            errorMessage="Your bid must be lower than your max bid"
                            formatOptions={{
                                style: 'currency',
                                currency: 'USD',
                                currencyDisplay: 'symbol',
                            }}
                        />
                        <NumberField
                            validationState={!isValid() ? 'invalid' : undefined}
                            label="Max bid"
                            value={maxBid}
                            onChange={setMaxBid}
                            hideStepper
                            minValue={
                                bid || auction.currentBid || auction.minimumBid
                            }
                            step={auction.bidIncrement || 0.5}
                            errorMessage="Your max bid must be higher than your bid"
                            formatOptions={{
                                style: 'currency',
                                currency: 'USD',
                                currencyDisplay: 'symbol',
                            }}
                        />
                        <Button
                            width="size-2000"
                            variant="cta"
                            isDisabled={!isValid()}
                            onPress={() => makeBid({ bid, maxBid })}
                        >
                            Make bid
                        </Button>
                    </Flex>
                </Content>
            </Dialog>
        </DialogTrigger>
    );
};
